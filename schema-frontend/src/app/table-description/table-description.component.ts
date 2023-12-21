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
  tablename:String='student11';
  column:any[]=[];
  primaryKeyVisibility: boolean = true;
  selectedPrimaryIndex: number | null = null;

  // columns = [
  //   { name: 'column.name', dataType: 'Type1', primary: false, editing: false },
  //   // { name: 'column.name', dataType: 'Type2', primary: true, editing: false },
  //   // Add more columns as needed
  // ];
  selectedDataType: string = '';
  

  constructor(
    private schemaGenerator: SchemaGeneratorService,
    private route:ActivatedRoute
  ){}
 
  ngOnInit(){
    this.route.paramMap.pipe(switchMap(params =>  {
       const tableName =params.get('tableName'); 
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
  

  saveColumn(column: any): void {
    // Save the changes and set the editing state to false
    
    this.column.forEach((col) => (col.editing = false));
    column.editing = false;
  }

  cancelEdit(column: any): void {
    // Cancel the edit and set the editing state to false
    this.column.forEach((col) => (col.editing = false));
    column.editing = false;
  }

OnSelectedDataType(event:any, columnIndex:number, column:Column){
  console.log(event);
  console.log(columnIndex);
  console.log(column);
  column.dataType=event.target.name;
  console.log(column.dataType);
  
}

toggleEditMode(): void {
  this.column.forEach((col) => (col.editing = true));
}
hasPrimaryKey(column: any): boolean {
  return this.column.some((col) => col.primary && col !== column);
}
}

