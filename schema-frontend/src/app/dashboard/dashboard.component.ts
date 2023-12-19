import { ChangeDetectorRef, Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPlus, faXmark } from '@fortawesome/free-solid-svg-icons';
import { Column } from '../Models/Column';
import { HttpClient } from '@angular/common/http';
import { SchemaGeneratorService } from '../Services/schema-generator.service';
import { ToastrService } from 'ngx-toastr';
import { ToasterService } from '../Services/toaster.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {


}

