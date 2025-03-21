import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTeamCardComponent } from './add-team-card.component';

describe('AddTeamCardComponent', () => {
  let component: AddTeamCardComponent;
  let fixture: ComponentFixture<AddTeamCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddTeamCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddTeamCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
