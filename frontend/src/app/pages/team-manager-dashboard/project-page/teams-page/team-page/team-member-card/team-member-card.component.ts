import { Component, Input, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarLinearComponent } from '../../../../../../shared/progress-bar-linear/progress-bar-linear.component';
import { ModalService } from '../../../../../../core/services/modal.service';
import { AssignTaskCardComponent } from '../../../../../../shared/assign-task-card/assign-task-card.component';
@Component({
  selector: 'app-team-member-card',
  standalone: true,
  imports: [CommonModule, ProgressBarLinearComponent, AssignTaskCardComponent],
  templateUrl: './team-member-card.component.html',
  styleUrls: ['./team-member-card.component.scss']
})
export class TeamMemberCardComponent implements OnInit, OnDestroy {
  @Input() id: number = 0;
  @Input() name: string = '';
  @Input() role: string = '';
  @Input() image: string = '';
  @Input() progress: number = 0;
  @Input() projectId: number = 0;
  
  @ViewChild('assignTaskModal') assignTaskModal!: AssignTaskCardComponent;
  
  showMenu: boolean = false;
  private clickListener: any;

  constructor(private modalService: ModalService) {}

  ngOnInit() {
    console.log("[TeamMemberCardComponent] id:", this.id);
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
    if (this.assignTaskModal) {
      this.assignTaskModal.open();
    } else {
      this.modalService.open({
        type: 'assignTask',
        data: { memberId: this.id }
      });
    }
  }

  get teamProgress(): number {
    return Math.floor(this.progress);
  }
  
}
