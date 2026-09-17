import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './aur-company.reducer';
import { IAurCompany } from 'app/shared/model/aur-company.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurCompany = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const aurCompanyList = useAppSelector(state => state.aurCompany.entities);
  const loading = useAppSelector(state => state.aurCompany.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="aur-company-heading" data-cy="AurCompanyHeading">
        <Translate contentKey="wmsApp.aurCompany.home.title">Aur Companies</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="wmsApp.aurCompany.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="wmsApp.aurCompany.home.createLabel">Create new Aur Company</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {aurCompanyList && aurCompanyList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="wmsApp.aurCompany.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurCompany.companyCode">Company Code</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurCompany.companyName">Company Name</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurCompany.erpType">Erp Type</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurCompany.apiEndPoint">Api EndPoint </Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurCompany.apiParameters">Api Parameters</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {aurCompanyList.map((aurCompany, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${aurCompany.id}`} color="link" size="sm">
                      {aurCompany.id}
                    </Button>
                  </td>
                  <td>{aurCompany.companyCode}</td>
                  <td>{aurCompany.companyName}</td>
                  <td>{aurCompany.erpType}</td>
                  <td>{aurCompany.apiEndPoint}</td>
                  <td>{aurCompany.apiParameters ? JSON.stringify(aurCompany.apiParameters) : ''}</td>

                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${aurCompany.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${aurCompany.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${aurCompany.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="wmsApp.aurCompany.home.notFound">No Aur Companies found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AurCompany;
