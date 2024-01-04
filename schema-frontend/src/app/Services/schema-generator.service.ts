import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments';
import { Column } from '../Models/Column';

@Injectable({
  providedIn: 'root'
})
export class SchemaGeneratorService {
 

  constructor(private http: HttpClient
  ) { }

  updateTableSchema(tableName: string, columns: Column[]): Observable<any> {
    return this.http.put(`${environment.backendUrl}schema/update-schema/${tableName}`, { columns });
  }
  generateTable(requestPayload:any):Observable<any>{
    console.log(requestPayload)
   return this.http.post<any>(`${environment.backendUrl}schema/create-table`, requestPayload)

  }
  fetchSchema(tablename:any):Observable<any>{
    tablename=tablename.toLowerCase();
    return this.http.get<any>(`${environment.backendUrl}schema/getSchema/${tablename}`);
  }

}
