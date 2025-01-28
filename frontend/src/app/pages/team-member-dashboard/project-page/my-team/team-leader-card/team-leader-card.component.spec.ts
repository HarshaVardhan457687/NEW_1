import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamLeaderCardComponent } from './team-leader-card.component';

describe('TeamLeaderCardComponent', () => {
  let component: TeamLeaderCardComponent;
  let fixture: ComponentFixture<TeamLeaderCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeamLeaderCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TeamLeaderCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
