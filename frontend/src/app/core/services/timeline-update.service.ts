import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { TimelineEvent } from './project-overview.service';

export interface CreateTimelineRequest {
  timeLineHeading: string;
  timeLineAssociatedProject: number;
  timeLineStatus: 'UPCOMING' | 'IN_PROGRESS' | 'COMPLETED';
}

export interface UpdateTimelineRequest {
  timeLineHeading?: string;
  timeLineStatus?: 'UPCOMING' | 'IN_PROGRESS' | 'COMPLETED';
}

@Injectable({
  providedIn: 'root'
})
export class TimelineUpdateService {
  private readonly API_URL = 'http://localhost:8060/api/timelines';

  constructor(private http: HttpClient) {}

  createTimeline(request: CreateTimelineRequest): Observable<TimelineEvent> {
    return this.http.post<TimelineEvent>(this.API_URL, request).pipe(
      tap(response => console.log('Create timeline response:', response))
    );
  }

  updateTimeline(timelineId: number, request: UpdateTimelineRequest): Observable<TimelineEvent> {
    return this.http.patch<TimelineEvent>(`${this.API_URL}/${timelineId}`, request).pipe(
      tap(response => console.log('Update timeline response:', response))
    );
  }

  deleteTimeline(timelineId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${timelineId}`).pipe(
      tap(() => console.log('Timeline deleted:', timelineId))
    );
  }
} 