import { Component, afterNextRender } from '@angular/core';
import { SchemaGeneratorService } from '../Services/schema-generator.service';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent {
  tablename:String='student11';
  column:any[]=[];
  cnames: string[] = [];

  constructor(
    private schemaGenerator: SchemaGeneratorService,
  ){}
 
  
  fetchSchema(){
    this.schemaGenerator.fetchSchema(this.tablename).subscribe({
      next: (response:any) => {
        console.log(response);
        
        this.column = response.columns;
        console.log(this.column);
        
        this.cnames = this.column.map((column:any)=>column.name);
        const columnDataTypes =this.column.map((column:any) =>column.dataType);
        const primaryKey = this.column.map((column:any)=> column.primary);

        console.log("column names",this.cnames);
        console.log("column datatypes", columnDataTypes);
        console.log(primaryKey);
        
        
      },
      error: (err) => {
        console.log("Error",err );
        
      }
    });
  }
  
  

}
