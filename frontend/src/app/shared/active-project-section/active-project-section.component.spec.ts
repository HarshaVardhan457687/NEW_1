import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveProjectSectionComponent } from './active-project-section.component';

describe('ActiveProjectSectionComponent', () => {
  let component: ActiveProjectSectionComponent;
  let fixture: ComponentFixture<ActiveProjectSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActiveProjectSectionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActiveProjectSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
