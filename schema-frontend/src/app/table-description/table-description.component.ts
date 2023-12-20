import { Component } from '@angular/core';
import { SchemaGeneratorService } from '../Services/schema-generator.service';

@Component({
  selector: 'app-table-description',
  templateUrl: './table-description.component.html',
  styleUrls: ['./table-description.component.css']
})
export class TableDescriptionComponent {
  tablename:String='student11';
  column:any[]=[];
  // cnames: string[] = [];
  // cDataTypes:string[] = [];
  // primaryKey:Boolean[] = [];

  constructor(
    private schemaGenerator: SchemaGeneratorService,
  ){}
 
  getDescription(){
    this.schemaGenerator.fetchSchema(this.tablename).subscribe({
      next:(response:any) =>{
        console.log(response);
        this.column = response.columns;
        console.log("data", this.column);
        
        
      },
      error:(err)=>{
         console.log(err);
         
      }
    })
  }
  
}

