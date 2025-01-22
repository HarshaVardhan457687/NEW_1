import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveProjectsCardComponent } from './active-projects-card.component';

describe('ActiveProjectsCardComponent', () => {
  let component: ActiveProjectsCardComponent;
  let fixture: ComponentFixture<ActiveProjectsCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActiveProjectsCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActiveProjectsCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
