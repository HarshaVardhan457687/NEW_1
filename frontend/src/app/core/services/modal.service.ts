import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  private isProfileModalOpenSubject = new BehaviorSubject<boolean>(false);
  isProfileModalOpen$ = this.isProfileModalOpenSubject.asObservable();

  openProfileModal() {
    this.isProfileModalOpenSubject.next(true);
  }

  closeProfileModal() {
    this.isProfileModalOpenSubject.next(false);
  }
} 