import { Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { Column } from '../Models/Column';
import { HttpClient } from '@angular/common/http';
import { SchemaGeneratorService } from '../Services/schema-generator.service';
import { ToastrService } from 'ngx-toastr';
import { ToasterService } from '../Services/toaster.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {
  tablename: string = '';
  columns: number = 0;
  inputValues: Column[] = []; // Use an array to store values for each input
  primary: boolean = false;
  primaryKeyVisibility: boolean = true;
  selectedPrimaryIndex: number | null = null;
  selectedDataType: string = 'Choose Data Type';

  constructor(
    private http: HttpClient,
    private schemaGenerator: SchemaGeneratorService,
    private toaster: ToasterService
  ) {}
  OnSelectedDataType(event: any, columnIndex: number, column: Column) {
    console.log(event);
    console.log(columnIndex);
    console.log(column);
    column.dataType = event.target.name;
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
          dataType: 'Choose Data Type',
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
      this.toaster.showSuccess;
      },
      error: (err) => {
        // console.log("erroro");
        
        console.log(err.message);
        this.toaster.showfailure;
      },
    });
  }


}

