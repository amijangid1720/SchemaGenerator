import { Component, OnInit } from '@angular/core';
import { TableService } from 'src/app/Services/tablenames.service';
import { faTable,faCirclePlus,faWrench } from '@fortawesome/free-solid-svg-icons';
@Component({
  selector: 'app-side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.css']
})
export class SideMenuComponent implements OnInit {
  tableNames: string[] = [];
  faTable=faTable;
  faPlus=faCirclePlus;
  faWrench=faWrench;


  constructor(private tableService: TableService) {}

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
}
