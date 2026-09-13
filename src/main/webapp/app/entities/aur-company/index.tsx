import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AurCompany from './aur-company';
import AurCompanyDetail from './aur-company-detail';
import AurCompanyUpdate from './aur-company-update';
import AurCompanyDeleteDialog from './aur-company-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AurCompanyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AurCompanyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AurCompanyDetail} />
      <ErrorBoundaryRoute path={match.url} component={AurCompany} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AurCompanyDeleteDialog} />
  </>
);

export default Routes;
