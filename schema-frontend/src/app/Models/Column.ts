export class Column {
  name: string = '';
  oldName: string = '';
  dataType: string = '';
  oldDataType: string = '';
  primary: boolean = false;
  oldPrimary: boolean = false;
  foreignKey: boolean = false;
  referencedTable: '';
  referencedColumn: '';
}
