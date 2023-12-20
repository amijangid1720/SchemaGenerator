import { Component, afterNextRender } from '@angular/core';
import { SchemaGeneratorService } from '../Services/schema-generator.service';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent {
  // tablename:String='';
//   column:any[]=[];
//   cnames: string[] = [];

//   constructor(
//     private schemaGenerator: SchemaGeneratorService,
//     private route: ActivatedRoute
//   ){}

//   ngOnInit() {
//     // Subscribe to route parameter changes
//     this.route.paramMap
//       .pipe(
//         switchMap(params => {
//           const tableName = params.get('tableName');
//          // Update the tablename property
//           return this.schemaGenerator.fetchSchema(tableName);
//         })
//       )
//       .subscribe({
//         next: (response: any) => {
//           console.log(response);

//           this.column = response.columns;
//           console.log(this.column);

//           this.cnames = this.column.map((column: any) => column.name);
//           const columnDataTypes = this.column.map(
//             (column: any) => column.dataType
//           );
//           const primaryKey = this.column.map((column: any) => column.primary);

//           console.log('column names', this.cnames);
//           console.log('column datatypes', columnDataTypes);
//           console.log(primaryKey);
//         },
//         error: (err) => {
//           console.log('Error', err);
//         }
//       });
//   }

column: any[] = [];
  cnames: string[] = [];
  columnData: any[] = []; // Initialize as an empty array

  constructor(
    private schemaGenerator: SchemaGeneratorService,
    private route: ActivatedRoute
  ) {}

      // Subscribe to route parameter changes
      ngOnInit() {
        // Subscribe to route parameter changes
        this.route.paramMap
          .pipe(
            switchMap(params => {
              const tableName = params.get('tableName');
              // Update the tablename property
              return this.schemaGenerator.fetchSchema(tableName);
            })
          )
          .subscribe({
            next: (response: any) => {
              console.log(response);
      
              this.column = response.columns;
              console.log(this.column);
      
              this.cnames = this.column.map((column: any) => column.name);
              // Initialize columnData with a single empty object
              this.columnData = [{}];
      
              const columnDataTypes = this.column.map(
                (column: any) => column.dataType
              );
              const primaryKey = this.column.map((column: any) => column.primary);
      
              console.log('column names', this.cnames);
              console.log('column datatypes', columnDataTypes);
              console.log(primaryKey);
            },
            error: (err) => {
              console.log('Error', err);
            }
          });
      }
      
    }