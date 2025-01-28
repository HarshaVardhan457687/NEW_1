import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamMemberSectionComponent } from './team-member-section.component';

describe('TeamMemberSectionComponent', () => {
  let component: TeamMemberSectionComponent;
  let fixture: ComponentFixture<TeamMemberSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeamMemberSectionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TeamMemberSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
