import { Component, Input , ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgressBarLinearComponent } from '../../../../../../shared/progress-bar-linear/progress-bar-linear.component';
import { AssignTaskCardComponent } from '../../../../../../shared/assign-task-card/assign-task-card.component';
import { ModalService } from '../../../../../../core/services/modal.service';

@Component({
  selector: 'app-team-leader-card',
  standalone: true,
  imports: [CommonModule, ProgressBarLinearComponent,AssignTaskCardComponent],
  templateUrl: './team-leader-card.component.html',
  styleUrls: ['./team-leader-card.component.scss']
})
export class TeamLeaderCardComponent {
  @Input() id: number = 0;
  @Input() name: string = '';
  @Input() profilePhoto: string = '';
  @Input() projectId: number = 0;

  @ViewChild('assignTaskModal') assignTaskModal!: AssignTaskCardComponent;
  
  showMenu: boolean = false;
  private clickListener: any;

  constructor(private modalService: ModalService) {}

  
  ngOnInit() {
    console.log("[TeamLeaderCardComponent] id:", this.id);
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
}
