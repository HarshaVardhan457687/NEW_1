import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyResultsListComponent } from './key-results-list.component';

describe('KeyResultsListComponent', () => {
  let component: KeyResultsListComponent;
  let fixture: ComponentFixture<KeyResultsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KeyResultsListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(KeyResultsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
