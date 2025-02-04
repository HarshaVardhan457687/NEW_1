import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamManagerDashboardComponent } from './team-manager-dashboard.component';

describe('TeamManagerDashboardComponent', () => {
  let component: TeamManagerDashboardComponent;
  let fixture: ComponentFixture<TeamManagerDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeamManagerDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TeamManagerDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
