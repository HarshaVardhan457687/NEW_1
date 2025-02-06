import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarLinearComponent } from '../../../../../shared/progress-bar-linear/progress-bar-linear.component';
import { ModalService } from '../../../../../core/services/modal.service';

@Component({
  selector: 'app-team-member-card',
  standalone: true,
  imports: [CommonModule, ProgressBarLinearComponent],
  templateUrl: './team-member-card.component.html',
  styleUrls: ['./team-member-card.component.scss']
})
export class TeamMemberCardComponent implements OnInit, OnDestroy {
  @Input() id: number = 0;
  @Input() name: string = '';
  @Input() role: string = '';
  @Input() image: string = '';
  @Input() progress: number = 0;
  
  showMenu: boolean = false;
  private clickListener: any;

  constructor(private modalService: ModalService) {}

  ngOnInit() {
    this.clickListener = (event: MouseEvent) => {
      if (!event.target) return;
      const target = event.target as HTMLElement;
      if (!target.closest('.menu-button') && !target.closest('.popup-menu')) {
        this.showMenu = false;
      }
    };
    document.addEventListener('click', this.clickListener);
  }

  ngOnDestroy() {
    document.removeEventListener('click', this.clickListener);
  }

  toggleMenu(event: Event) {
    event.stopPropagation();
    this.showMenu = !this.showMenu;
  }

  onAssignTask() {
    this.modalService.open({
      type: 'assignTask',
      data: { memberId: this.id }  // Pass any relevant data
    });
  }
}
