import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './aur-role.reducer';
import { IAurRole } from 'app/shared/model/aur-role.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurRole = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const aurRoleList = useAppSelector(state => state.aurRole.entities);
  const loading = useAppSelector(state => state.aurRole.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="aur-role-heading" data-cy="AurRoleHeading">
        <Translate contentKey="wmsApp.aurRole.home.title">Aur Roles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="wmsApp.aurRole.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="wmsApp.aurRole.home.createLabel">Create new Aur Role</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {aurRoleList && aurRoleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="wmsApp.aurRole.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurRole.roleId">Role Id</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurRole.roleName">Role Name</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurRole.companyCode">Company Code</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {aurRoleList.map((aurRole, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${aurRole.id}`} color="link" size="sm">
                      {aurRole.id}
                    </Button>
                  </td>
                  <td>{aurRole.roleId}</td>
                  <td>{aurRole.roleName}</td>
                  <td>{aurRole.companyCode}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${aurRole.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${aurRole.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${aurRole.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="wmsApp.aurRole.home.notFound">No Aur Roles found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AurRole;
