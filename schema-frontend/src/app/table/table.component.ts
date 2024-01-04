import { Component, afterNextRender } from '@angular/core';
import { SchemaGeneratorService } from '../Services/schema-generator.service';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs';
import { TableRow } from '../Models/table-row';
import { TableService } from '../Services/table.service';
import { FormsModule } from '@angular/forms';
import { ParseError } from '@angular/compiler';
import { ToasterService } from '../Services/toaster.service';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css'],
})
export class TableComponent {
  column: any[] = [];
  cnames: string[] = [];
  columnNames: string[] = [];
  tableName: any;
  newRow: any = {};
  columnData: any[] = []; // Initialize as an empty array
  

  constructor(
    private schemaGenerator: SchemaGeneratorService,
    private route: ActivatedRoute,
    private tableService: TableService,
    private toaster: ToasterService
  ) {}

  // Subscribe to route parameter changes
  ngOnInit() {
    this.route.paramMap
      .pipe(
        switchMap((params) => {
          this.tableName = params.get('tableName');
          return this.schemaGenerator.fetchSchema(this.tableName);
        })
      )
      .subscribe({
        next: (response: any) => {
          //console.log(response);

          this.column = response.columns;
          //console.log(this.column);

          this.columnNames = this.column.map((column: any) => column.name);

          // Fetch and display data from the backend
          this.tableService.fetchTableData(this.tableName).subscribe({
            next: (data: any[]) => {
              //console.log(data);
              this.columnData = data;
            },
            error: (error) => {
              console.error('Error fetching table data', error);
            },
          });
        },
        error: (err) => {
          //console.log('Error fetching schema', err);
        },
      });
  }
  addRow() {
    // Iterate through columns and convert values based on data types
    for (const column of this.column) {
      const columnName = column.name;
      const columnDataType = column.dataType;

      // Check if the newRow contains the current column
      if (this.newRow.hasOwnProperty(columnName)) {
        // Convert value based on data type
        this.newRow[columnName] = this.convertToDataType(
          this.newRow[columnName],
          columnDataType
        );
      }
    }

    // Send the parsed newRow to the backend
    this.tableService.addRow(this.tableName, this.newRow).subscribe(
      (response: any) => {
        //console.log('Row added successfully', response);
        this.toaster.dataSuccess();

        // Optionally, you can update this part based on your use case
        this.columnData.push({ ...this.newRow }); // Add the new row to the table
        this.newRow = {}; // Clear the new row for the next input
        //console.log("newRow:", this.columnData);
      },
      (error) => {
        //console.log("Failed to add row", error);
        this.toaster.dataAddingFailed();
      }
    );
  }

  private convertToDataType(value: any, dataType: string): any {
    switch (dataType) {
      case 'integer':
        return parseInt(value, 10);
      case 'boolean':
        return value === 'true' || value === true;
      case 'character varying':
        return String(value);
      case 'date':
        // Assuming the date is in a standard string format, adjust as needed
        return new Date(value);
      case 'double precision':
        return parseFloat(value);
      case 'real':
        return parseFloat(value);
      default:
        return value;
    }
  }
}
