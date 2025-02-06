import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type ModalType = 'profile' | 'assignTask';

export interface ModalConfig {
  type: ModalType;
  data?: any;
}

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  private isProfileModalOpenSubject = new BehaviorSubject<boolean>(false);
  isProfileModalOpen$ = this.isProfileModalOpenSubject.asObservable();

  private modalConfigSubject = new BehaviorSubject<ModalConfig | null>(null);
  modalConfig$ = this.modalConfigSubject.asObservable();

  openProfileModal() {
    this.isProfileModalOpenSubject.next(true);
  }

  closeProfileModal() {
    this.isProfileModalOpenSubject.next(false);
  }

  open(config: ModalConfig) {
    this.modalConfigSubject.next(config);
  }

  close() {
    this.modalConfigSubject.next(null);
  }
} 