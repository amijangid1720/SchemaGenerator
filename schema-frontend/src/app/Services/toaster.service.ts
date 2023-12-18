import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ToasterService {

  constructor(
    private toastr: ToastrService
  ) { }

  showSuccess() {
    this.toastr.success('Table created successfully!', 'Success!');
  }
  showfailure(){
    this.toastr.error('Failed to create table!','Failed!');
  }
}
