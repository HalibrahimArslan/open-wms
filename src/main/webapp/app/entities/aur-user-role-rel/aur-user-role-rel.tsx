import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './aur-user-role-rel.reducer';
import { IAurUserRoleRel } from 'app/shared/model/aur-user-role-rel.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurUserRoleRel = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const aurUserRoleRelList = useAppSelector(state => state.aurUserRoleRel.entities);
  const loading = useAppSelector(state => state.aurUserRoleRel.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="aur-user-role-rel-heading" data-cy="AurUserRoleRelHeading">
        <Translate contentKey="wmsApp.aurUserRoleRel.home.title">Aur User Role Rels</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="wmsApp.aurUserRoleRel.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="wmsApp.aurUserRoleRel.home.createLabel">Create new Aur User Role Rel</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {aurUserRoleRelList && aurUserRoleRelList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="wmsApp.aurUserRoleRel.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurUserRoleRel.userId">User Id</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurUserRoleRel.roleId">Role Id</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {aurUserRoleRelList.map((aurUserRoleRel, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${aurUserRoleRel.id}`} color="link" size="sm">
                      {aurUserRoleRel.id}
                    </Button>
                  </td>
                  <td>{aurUserRoleRel.userId}</td>
                  <td>{aurUserRoleRel.roleId}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${aurUserRoleRel.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${aurUserRoleRel.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${aurUserRoleRel.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="wmsApp.aurUserRoleRel.home.notFound">No Aur User Role Rels found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AurUserRoleRel;
