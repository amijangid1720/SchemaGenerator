import { Component, OnInit } from '@angular/core';
import { faTable,faCirclePlus,faWrench, faPlus, faGreaterThan, faTrashCan } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { ConfirmEventType, ConfirmationService, MessageService } from 'primeng/api';
import { TableService } from 'src/app/Services/table.service';
@Component({
  selector: 'app-side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.css']
})
export class SideMenuComponent implements OnInit {
  tableNames: string[] = [];
  faTable=faTable;
  faPlus=faPlus;
  faWrench=faWrench;
  faGreaterThan=faGreaterThan;
  faTrashCan=faTrashCan;

  isCollapsed = false;


  constructor(private tableService:TableService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private toastr: ToastrService
    ) {}

  ngOnInit(): void {
    this.tableService.tableNames$.subscribe((tableNames) => {
      this.tableNames = tableNames;
    });
  }

  

  deleteTable(tableName:any){
    this.confirmationService.confirm({  
      accept: () => {
        console.log(tableName);
        this.tableService.deleteTable(tableName).subscribe(
          (data) => {
          this.tableNames= this.tableNames.filter((element)=>{
              return element!=tableName;
            })
            this.toastr.success('Table Deleted successfully!', 'Success!');
          },
          (error:any) => {
            console.error('Error deleting contact:', error);
            this.toastr.error('Some Error Ocuured', 'Error!');
          }
        );
      },
      reject: (type: ConfirmEventType) => {
        switch (type) {
          case ConfirmEventType.REJECT:
            
            break;
          case ConfirmEventType.CANCEL:
            
            break;
        }
      },
    });

  }


  toggleCollapse() {
    this.isCollapsed = !this.isCollapsed;
  }
}
