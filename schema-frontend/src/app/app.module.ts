import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { InputNumberModule } from 'primeng/inputnumber';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { NgxUiLoaderModule, NgxUiLoaderHttpModule } from "ngx-ui-loader";
import { TooltipModule } from 'primeng/tooltip';
import { CreateTableComponent } from './dashboard/create-table/create-table.component';
import { SideMenuComponent } from './dashboard/side-menu/side-menu.component';
import { TableComponent } from './table/table.component';
import { TableDescriptionComponent } from './table-description/table-description.component';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';



@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    CreateTableComponent,
    SideMenuComponent,
    TableComponent,
    TableDescriptionComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    InputTextModule,
    InputNumberModule,
    FontAwesomeModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    NgxUiLoaderModule,
    NgxUiLoaderHttpModule.forRoot({ showForeground: true}),
    TooltipModule,
    ConfirmDialogModule,
  ],
  providers: [ MessageService,ConfirmationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
