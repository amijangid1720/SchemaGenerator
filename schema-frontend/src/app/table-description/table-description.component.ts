import { Component } from '@angular/core';
import { SchemaGeneratorService } from '../Services/schema-generator.service';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs';
import { Column } from '../Models/Column';

@Component({
  selector: 'app-table-description',
  templateUrl: './table-description.component.html',
  styleUrls: ['./table-description.component.css']
})
export class TableDescriptionComponent {
  tablename:string='';
  column:any[]=[];
  primaryKeyVisibility: boolean = true;
  selectedPrimaryIndex: number | null = null;
  selectedDataType: string = '';
  oldCol:any[]=[];
  

  constructor(
    private schemaGenerator: SchemaGeneratorService,
    private route:ActivatedRoute
  ){}
 
  ngOnInit(){
    this.route.paramMap.pipe(switchMap(params =>  {
       const tableName =params.get('tableName');
       this.tablename=params.get('tableName');
       return this.schemaGenerator.fetchSchema(tableName);     
    }))
    .subscribe({next:(response:any) =>{
      console.log(response);
      this.column=response.columns;
      console.log("data", this.column);
    },
    error:(err)=>{
      console.log(err);
      
    }

    })
  }

  // editColumn(column: any): void {
  //   // Set the editing state to true
  //   column.editing = true;
  // }
  checkVisibility(index: number) {
    this.selectedPrimaryIndex = index;
    this.column.forEach((column, i) => {
      column.primary = i === index;
    });
  }

  uneditable(event: Event, index: number) {
    event.preventDefault();
    console.log('Primary key clicked:', index);
    // Your existing logic for updating primary keys can go here
  }
  

  saveTable(): void {
    // Assuming you have a service method named updateTableSchema in SchemaGeneratorService
    this.schemaGenerator.updateTableSchema(this.tablename, this.column)
      .subscribe({
        next: (response) => {
          console.log(response);
          // Handle the response, show success message, etc.
        },
        error: (error) => {
          console.error(error);
          // Handle the error, show error message, etc.
        }
      });
  }

  cancelEdit(column: any): void {
    // Cancel the edit and set the editing state to false
    this.column.forEach((col) => {
      col.editing = false;
      // Revert changes by restoring the values from the backup
      col.name = col.oldName;
      col.dataType = col.olddataType;
      col.primary = col.oldIsPrimary;
    });
  
    
  }

OnSelectedDataType(event:any, columnIndex:number, column:Column){
  console.log(event);
  console.log(columnIndex);
  console.log(column);
  column.dataType=event.target.name;
  console.log(column.dataType);
  
}

toggleEditMode(): void {
  this.column.forEach((col) => {
    col.editing = true;
    col.oldName = col.name;
    col.olddataType = col.dataType;
    col.oldIsPrimary = col.primary;
  });
  this.oldCol = this.column.map((col) => ({ ...col }));
}
hasPrimaryKey(column: any): boolean {
  return this.column.some((col) => col.primary && col !== column);
}
}

