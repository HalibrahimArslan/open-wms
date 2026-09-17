import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';
import administration, { AdministrationState } from '../../modules/administration/administration.reducer';
import userManagement, { UserManagementState } from '../../modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from '../../modules/account/register/register.reducer';
import activate, { ActivateState } from '../../modules/account/activate/activate.reducer';
import password, { PasswordState } from '../../modules/account/password/password.reducer';
import settings, { SettingsState } from '../../modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from '../../modules/account/password-reset/password-reset.reducer';

// prettier-ignore
import aurCompany from '../../entities/aur-company/aur-company.reducer';

/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  aurCompany,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
