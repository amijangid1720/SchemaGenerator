import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../environments';

@Injectable({
  providedIn: 'root',
})
export class TableService {
  private tableNamesSubject: BehaviorSubject<string[]> = new BehaviorSubject<string[]>([]);
  public tableNames$: Observable<string[]> = this.tableNamesSubject.asObservable();

  constructor(private http: HttpClient) {
    this.getTableNames();
   }

  
  getTableNames():any {
    this.http.get<string[]>(`${environment.backendUrl}schema/tables`).subscribe(
      (tableNames) => {
        this.tableNamesSubject.next(tableNames);
      },
      (error) => {
        console.error('Error fetching table names', error);
      }
    );
  }

  deleteTable(tableName:any):Observable<any>{
    return this.http.delete<any>(`${environment.backendUrl}schema/deleteTable/${tableName}`);
  }

  fetchTableData(tableName: string): Observable<any[]> {
    const url = `${environment.backendUrl}schema/getData/${tableName}`; // Adjust the endpoint as needed
    return this.http.get<any[]>(url);
  }
}