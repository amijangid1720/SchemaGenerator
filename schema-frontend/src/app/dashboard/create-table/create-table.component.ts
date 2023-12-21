import { ChangeDetectorRef, Component } from '@angular/core';
import { SchemaGeneratorService } from '../../Services/schema-generator.service';
import { HttpClient } from '@angular/common/http';
import { faPlus, faXmark } from '@fortawesome/free-solid-svg-icons';
import { ToasterService } from '../../Services/toaster.service';
import { Column } from '../../Models/Column';
import { TableService } from 'src/app/Services/table.service';


@Component({
  selector: 'app-create-table',
  templateUrl: './create-table.component.html',
  styleUrls: ['./create-table.component.css']
})
export class CreateTableComponent {
  faXmark=faXmark;
  tablename: string = '';
  columns: number = 0;
  inputValues: Column[] = []; // Use an array to store values for each input
  primary: boolean = false;
  primaryKeyVisibility: boolean = true;
  selectedPrimaryIndex: number | null = null;
  selectedDataType: string = '';


  constructor(
    private http: HttpClient,
    private schemaGenerator: SchemaGeneratorService,
    private toaster: ToasterService,
    private cdr: ChangeDetectorRef ,
    private tableService:TableService
  ) {}
  OnSelectedDataType(event: any, columnIndex: number, column: Column) {
    console.log(event);
    console.log(columnIndex);
    console.log(column);
    column.dataType = event.target.name;
  }
  reduceColumns(){
    this.columns=this.columns-1;
    
  }
  onColumnsChange(val: any) {
    const currentColumnCount = this.inputValues.length;
    // If the number of columns has increased, add new columns
    if (this.columns > currentColumnCount) {
      const columnsToAdd = this.columns - currentColumnCount;
      for (let i = 0; i < columnsToAdd; i++) {
        const newColumn: Column = {
          name: '',
          primary: false,
          dataType: '',
        };
        this.inputValues.push(newColumn);
      }
    }
    // If the number of columns has decreased, remove extra columns
    else if (this.columns < currentColumnCount) {
      const columnsToRemove = currentColumnCount - this.columns;
      this.inputValues.splice(
        this.inputValues.length - columnsToRemove,
        columnsToRemove
      );
    }
  }
  checkVisibility(index: number) {
    this.selectedPrimaryIndex = index;
    this.inputValues.forEach((column, i) => {
      column.primary = i === index;
    });
  }


  generateTable1() {
   
    // Prepare the request payload
    const requestPayload = {
      tableName: this.tablename,
      columns: this.inputValues.map((column) => ({
        name: column.name,
        primary: column.primary,
        dataType: column.dataType,
      })),
    };
    this.schemaGenerator.generateTable(requestPayload).subscribe({
      next: (response) => {
        console.log(response);
        this.tableService.getTableNames();
        
      this.toaster.showSuccess();
      },
      error: (err) => {
         console.log("error");
        
        console.log(err.message);
        this.toaster.showfailure();
      },
    });
  
  }

  // Function to delete a column based on its index
  deleteColumn(index: number) {
    // Remove the column at the specified index
    this.inputValues.splice(index, 1);

    // Reset selectedPrimaryIndex if the deleted column was the primary key
    if (this.selectedPrimaryIndex === index) {
      this.selectedPrimaryIndex = null;
    }

    // Trigger change detection
    this.cdr.detectChanges();
  }

}
