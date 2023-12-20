import { Component } from '@angular/core';
import { SchemaGeneratorService } from '../Services/schema-generator.service';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs';

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

}

