import { Component, afterNextRender } from '@angular/core';
import { SchemaGeneratorService } from '../Services/schema-generator.service';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs';
import { TableRow } from '../Models/table-row';
import { TableService } from '../Services/table.service';
import { FormsModule } from '@angular/forms'; 


@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})


export class TableComponent {
 
column: any[] = [];
  cnames: string[] = [];
  columnNames: string[] = [];
  tableName:any
  columnData: any[] = []; // Initialize as an empty array
  // newRow: any = {};
  // columnDatas: TableRow[] = [{}];
  
  constructor(
    private schemaGenerator: SchemaGeneratorService,
    private route: ActivatedRoute,
    private tableService: TableService
  ) {}

      // Subscribe to route parameter changes
      ngOnInit() {
        // Subscribe to route parameter changes
      //   this.route.paramMap
      //     .pipe(
      //       switchMap(params => {
      //         const tableName = params.get('tableName');
      //         // Update the tablename property
      //         return this.schemaGenerator.fetchSchema(tableName);
      //       })
      //     )
      //     .subscribe({
      //       next: (response: any) => {
      //         console.log(response);
      
      //         this.column = response.columns;
      //         console.log(this.column);
      
      //         this.cnames = this.column.map((column: any) => column.name);
      //         // Initialize columnData with a single empty object
      //         this.columnData = [{}];
      
      //         const columnDataTypes = this.column.map(
      //           (column: any) => column.dataType
      //         );
      //         const primaryKey = this.column.map((column: any) => column.primary);
      
      //         console.log('column names', this.cnames);
      //         console.log('column datatypes', columnDataTypes);
      //         console.log(primaryKey);
      //       },
      //       error: (err) => {
      //         console.log('Error', err);
      //       }
      //     });
      // }

      this.route.paramMap.pipe(
        switchMap(params => {
         this.tableName = params.get('tableName');
          return this.schemaGenerator.fetchSchema(this.tableName);
        })
      ).subscribe({
        next: (response: any) => {
          console.log(response);
  
          this.column = response.columns;
          console.log(this.column);
  
          this.columnNames = this.column.map((column: any) => column.name);
  
          // Fetch and display data from the backend
          this.tableService.fetchTableData(this.tableName).subscribe({
            next: (data: any[]) => {
              console.log(data);
              this.columnData = data;
            },
            error: (error) => {
              console.error('Error fetching table data', error);
            }
          });
        },
        error: (err) => {
          console.log('Error fetching schema', err);
        }
      });
    }

    

      // onCellInput(event: any, rowIndex: number, columnName: string) {
      //   // Update the corresponding value in the table data
      //   this.columnData[rowIndex][columnName] = event.target.textContent;
      // }

      // applyChanges() {
      //   console.log(this.columnData);
        
      //   this.tableService.saveData(this.columnData).subscribe(
      //     (response) => {
      //       // Handle successful response from the backend
      //       console.log('Data saved successfully:', response);
      //     },
      //     (error) => {
      //       // Handle error from the backend
      //       console.error('Error saving data:', error);
      //     }
      //   );
      // }
    
    }