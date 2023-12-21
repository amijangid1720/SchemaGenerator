import { Component, OnInit } from '@angular/core';
import { faTable,faCirclePlus,faWrench, faPlus, faGreaterThan, faTrashCan } from '@fortawesome/free-solid-svg-icons';
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


  constructor(private tableService:TableService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    ) {}

  ngOnInit(): void {
    this.tableService.tableNames$.subscribe((tableNames) => {
      this.tableNames = tableNames;
    });
  }

  getTableNames(): void {
    // this.tableService.getTableNames().subscribe(names => {
    //   this.tableNames = names;
    // });
  }

  deleteTable(tableNames:any){
    console.log(tableNames);
    

    this.confirmationService.confirm({  
      accept: () => {
        const table = this.tableNames;
        console.log(table);
        this.tableService.deleteTable(table).subscribe(
          () => {
            this.messageService.add({
              severity: 'info',
              summary: 'Deleted',
              detail: 'Contact Deleted',
            });
            setTimeout(() => {
              window.location.reload();
            }, 1500);
          },
          (error:any) => {
            console.error('Error deleting contact:', error);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Error deleting contact',
            });
          }
        );
      },
      reject: (type: ConfirmEventType) => {
        switch (type) {
          case ConfirmEventType.REJECT:
            this.messageService.add({
              severity: 'error',
              summary: 'Rejected',
              detail: 'You have rejected',
            });
            break;
          case ConfirmEventType.CANCEL:
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'You have cancelled',
            });
            break;
        }
      },
    });

  }
}
