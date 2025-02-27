import { CommonModule } from '@angular/common';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProfilePageService, ProfileData } from '../../core/services/profile-page.service';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss']
})
export class EditProfileComponent implements OnInit {
  @Output() close = new EventEmitter<void>();
  @Output() formSubmit = new EventEmitter<any>();

  registrationForm!: FormGroup;
  submitted = false;
  timezones: { value: string; label: string }[] = [];
  countryCodes: { value: string; label: string }[] = [
    { value: '+91', label: '+91 (India)' },
    { value: '+1', label: '+1 (USA)' },
    { value: '+44', label: '+44 (UK)' },
    // Add more country codes as needed
  ];

  constructor(
    private formBuilder: FormBuilder,
    private profileService: ProfilePageService
  ) {}

  ngOnInit(): void {
    this.registrationForm = this.createForm();
    this.loadTimezones();
    this.prefillFormFromSessionStorage();
  }

  private prefillFormFromSessionStorage(): void {
    const profileData = sessionStorage.getItem('user_profile');
    if (!profileData) return;

    const profile: ProfileData = JSON.parse(profileData);
    
    // Only set values that are not null
    const formValues: any = {};
    
    if (profile.name) formValues.name = profile.name;
    if (profile.email) formValues.email = profile.email;
    if (profile.title) formValues.designation = profile.title;
    if (profile.location) formValues.address = profile.location;
    if (profile.timezone) formValues.timezone = profile.timezone;
    if (profile.joiningDate) formValues.joiningDate = profile.joiningDate;
    
    // Handle phone number - now expecting number with '+' prefix
    if (profile.phone) {
      // Phone already includes '+', so just split it
      formValues.countryCode = profile.phone.substring(0, 3); // Get +91, +1, etc.
      formValues.phoneNumber = profile.phone.substring(3); // Get rest of the number
    }

    // Only patch values that exist
    if (Object.keys(formValues).length > 0) {
      this.registrationForm.patchValue(formValues);
    }
  }

  private createForm(): FormGroup {
    return this.formBuilder.group({
      name: ['', [Validators.required, Validators.pattern('^[a-zA-Z ]*$')]],
      email: ['', [Validators.required, Validators.email]],
      countryCode: ['+91', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
      address: ['', Validators.required],
      designation: ['', Validators.required],
      timezone: ['', Validators.required],
      joiningDate: ['', Validators.required]
    });
  }

  loadTimezones(): void {
    try {
      // @ts-ignore
      const tzNames = Intl.supportedValuesOf('timeZone');
      this.timezones = tzNames.map((tz: string) => ({
        value: tz,
        label: `(${this.getTimezoneOffset(tz)}) ${tz.replace(/_/g, ' ')}`
      })).sort((a, b) => a.label.localeCompare(b.label));
    } catch (error) {
      this.timezones = [
        { value: 'UTC', label: '(UTC+00:00) Coordinated Universal Time' },
        { value: 'America/New_York', label: '(UTC-05:00) Eastern Time (US & Canada)' },
        { value: 'America/Chicago', label: '(UTC-06:00) Central Time (US & Canada)' },
        { value: 'America/Los_Angeles', label: '(UTC-08:00) Pacific Time (US & Canada)' },
        { value: 'Europe/London', label: '(UTC+00:00) London, Edinburgh' },
        { value: 'Asia/Tokyo', label: '(UTC+09:00) Tokyo' },
        { value: 'Australia/Sydney', label: '(UTC+10:00) Sydney, Melbourne' }
      ];
    }
  }

  getTimezoneOffset(timezone: string): string {
    try {
      const now = new Date();
      const tzString = now.toLocaleString('en-US', { timeZone: timezone });
      const localString = now.toLocaleString('en-US');
      const tzDate = new Date(tzString);
      const localDate = new Date(localString);
      let offsetInMinutes = (tzDate.getTime() - localDate.getTime()) / 60000;
      offsetInMinutes += now.getTimezoneOffset();
      const hours = Math.floor(Math.abs(offsetInMinutes) / 60);
      const minutes = Math.abs(offsetInMinutes) % 60;
      return `UTC${offsetInMinutes >= 0 ? '+' : '-'}${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
    } catch (error) {
      return 'UTC';
    }
  }

  get f() {
    return this.registrationForm.controls;
  }

  onSubmit(): void {
    console.log('Form submitted', this.registrationForm.value);
    this.submitted = true;
    if (this.registrationForm.invalid) {
      console.log('Form is invalid', this.registrationForm.errors);
      return;
    }
    this.formSubmit.emit(this.registrationForm.value);
    this.closePopup();
  }

  closePopup(): void {
    this.close.emit();
  }

  resetForm(): void {
    this.submitted = false;
    this.registrationForm.reset();
  }
}