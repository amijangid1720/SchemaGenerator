import { Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { Column } from '../Models/Column';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {
  tablename: string = '';
  columns: number = 0;
  inputValues: Column[] = []; // Use an array to store values for each input
  OnSelectedDataType(event: any, columnIndex: number, column: Column) {
    console.log(event);
    console.log(columnIndex);
    console.log(column);
    column.dataType = event.target.name;
  }

  onColumnsChange(val: any) {
    const currentColumnCount = this.inputValues.length;
    // If the number of columns has increased, add new columns
    if (this.columns > currentColumnCount) {
      const columnsToAdd = this.columns - currentColumnCount;

      for (let i = 0; i < columnsToAdd; i++) {
        const newColumn: Column = {
          name: '',
          isPrimary: false,
          dataType: 'Choose Data Type',
        };

        this.inputValues.push(newColumn);
      }
    }
    // If the number of columns has decreased, remove extra columns
    else if (this.columns < currentColumnCount) {
      const columnsToRemove = currentColumnCount - this.columns;

      this.inputValues.splice(
        this.inputValues.length - columnsToRemove,
        columnsToRemove
      );
    }
  }
}
