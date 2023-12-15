import { Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPlus} from '@fortawesome/free-solid-svg-icons';


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
}
