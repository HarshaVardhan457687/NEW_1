import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectTeamCardComponent } from './project-team-card.component';

describe('ProjectTeamCardComponent', () => {
  let component: ProjectTeamCardComponent;
  let fixture: ComponentFixture<ProjectTeamCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectTeamCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjectTeamCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
