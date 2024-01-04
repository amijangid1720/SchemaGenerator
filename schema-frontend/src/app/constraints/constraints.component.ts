import { Component, OnInit } from '@angular/core';
import { SchemaGeneratorService } from '../Services/schema-generator.service';
import { ActivatedRoute } from '@angular/router';
import { TableService } from '../Services/table.service';
import { MessageService } from 'primeng/api';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-constraints',
  templateUrl: './constraints.component.html',
  styleUrls: ['./constraints.component.css'],
})
export class ConstraintsComponent implements OnInit {
  columnNames: string[] = [];
  selectedColumn: any;
  tableName:string;
  referencedTable:string;
  referencedColumn:string;

  constructor(
    private schemaService: SchemaGeneratorService,
    private tableService: TableService,
    private route: ActivatedRoute,
    private toastrService: ToastrService
  ) {}
  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.tableName = params['tableName'];
      // Fetch the columns of the selected table using your data service
      this.schemaService.fetchSchema(this.tableName).subscribe((columns) => {
        const data = columns['columns'];
        this.columnNames = [];
        data.forEach((column) => {
          this.columnNames.push(column.name);
        });
      });
    });
  }

  addKey() {
    
    const request = {
      tableName: this.tableName,
      columnName: this.selectedColumn,
      referencedTable:this.referencedTable ,
      referencedColumn:this.referencedColumn,
    };

    this.tableService.addForeignKey(request).subscribe(
      (response) => {
        console.log(response);
        this.toastrService.success('Foreign Key Added')
      },
      (error) => {
        console.error(error); // Handle error
      }
    );
  }
}
