import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './aur-menu-role-rel.reducer';
import { IAurMenuRoleRel } from 'app/shared/model/aur-menu-role-rel.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurMenuRoleRel = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const aurMenuRoleRelList = useAppSelector(state => state.aurMenuRoleRel.entities);
  const loading = useAppSelector(state => state.aurMenuRoleRel.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="aur-menu-role-rel-heading" data-cy="AurMenuRoleRelHeading">
        <Translate contentKey="wmsApp.aurMenuRoleRel.home.title">Aur Menu Role Rels</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="wmsApp.aurMenuRoleRel.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="wmsApp.aurMenuRoleRel.home.createLabel">Create new Aur Menu Role Rel</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {aurMenuRoleRelList && aurMenuRoleRelList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="wmsApp.aurMenuRoleRel.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurMenuRoleRel.menuId">Menu Id</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurMenuRoleRel.roleId">Role Id</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {aurMenuRoleRelList.map((aurMenuRoleRel, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${aurMenuRoleRel.id}`} color="link" size="sm">
                      {aurMenuRoleRel.id}
                    </Button>
                  </td>
                  <td>{aurMenuRoleRel.menuId}</td>
                  <td>{aurMenuRoleRel.roleId}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${aurMenuRoleRel.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${aurMenuRoleRel.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${aurMenuRoleRel.id}/delete`}
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
              <Translate contentKey="wmsApp.aurMenuRoleRel.home.notFound">No Aur Menu Role Rels found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AurMenuRoleRel;
