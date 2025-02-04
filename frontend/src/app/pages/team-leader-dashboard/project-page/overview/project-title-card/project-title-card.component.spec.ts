import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectTitleCardComponent } from './project-title-card.component';

describe('ProjectTitleCardComponent', () => {
  let component: ProjectTitleCardComponent;
  let fixture: ComponentFixture<ProjectTitleCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectTitleCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProjectTitleCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
