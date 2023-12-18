import { Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPlus} from '@fortawesome/free-solid-svg-icons';
import { HttpClient } from '@angular/common/http';



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  tablename:string='';
  columns: number=0;
  inputValues: string[] = []; // Use an array to store values for each input
  isPrimary:boolean=false;
  selectedDataType:string='Choose Data Type';
  OnSelectedDataType(event:any)
  {
     console.log(event);
     this.selectedDataType=event.target.innerText;
     console.log(this.selectedDataType);
     
     
  }

  constructor(private http: HttpClient){
  }

  generateTable() {
    // Prepare the request payload
    const requestPayload = {
      tableName: this.tablename,
      columns: this.generateColumns()
    };
     console.log("11111");
     
    // Make a POST request to your Spring Boot backend
    this.http.post<any>('http://localhost:8082/schema/create-table', requestPayload)
      .subscribe(response => {
        console.log("222222");
        
        console.log("response",response);
        
      });
  }


  generateColumns(){
    const columns =[];
    for(let i =0; i<this.columns; i++){
      const column ={
        name:this.inputValues[i],
        datatype:this.selectedDataType,
        primaryKey:this.isPrimary && i == 0
      };
      columns.push(column);
    }
    return columns;
  }

}
