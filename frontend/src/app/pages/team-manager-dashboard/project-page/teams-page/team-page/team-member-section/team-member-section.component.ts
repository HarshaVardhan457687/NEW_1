import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TeamMemberCardComponent } from '../team-member-card/team-member-card.component';
import { TeamMemberProgress } from '../../../../../../core/services/my-team.service';

@Component({
  selector: 'app-team-member-section',
  standalone: true,
  imports: [CommonModule, TeamMemberCardComponent],
  templateUrl: './team-member-section.component.html',
  styleUrls: ['./team-member-section.component.scss']
})
export class TeamMemberSectionComponent {
  @Input() members: TeamMemberProgress[] = [];
}
