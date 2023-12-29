import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';


@Injectable({
  providedIn: 'root'
})
export class ToasterService {

  constructor(
    private toastr: ToastrService
  ) { }

  showError(msg,err){
    this.toastr.error(msg,err);
  }
  showSuccess(msg,success) {
    this.toastr.success(msg,success);
  }
 
}
