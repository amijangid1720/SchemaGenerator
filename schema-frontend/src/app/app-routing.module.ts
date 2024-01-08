import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CreateTableComponent } from './dashboard/create-table/create-table.component';
import { TableComponent } from './table/table.component';
import { TableDescriptionComponent } from './table-description/table-description.component';
import { ConstraintsComponent } from './constraints/constraints.component';

const routes: Routes = [
  {
    path:'',
  component:DashboardComponent,
  children:[
    {
      path:'createtable',
      component:CreateTableComponent,
    },
    {
      path: 'table/:tableName', 
      component: TableComponent,
    },
    {
      path:'tabledesc/:tableName',
      component:TableDescriptionComponent,
    },
    {
      path:'addForeignKey/:tableName',
      component:ConstraintsComponent
    }

   
  ]
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
