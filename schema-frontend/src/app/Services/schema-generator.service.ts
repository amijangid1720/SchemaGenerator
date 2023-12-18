import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments';

@Injectable({
  providedIn: 'root'
})
export class SchemaGeneratorService {

  constructor(private http: HttpClient
  ) { }


  generateTable(requestPayload:any):Observable<any>{
   return this.http.post<any>(`${environment.backendUrl}schema/create-table`, requestPayload)

  }

}
