import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Translate } from 'react-jhipster';
import { Alert } from 'reactstrap';
import { Button, Row, Col, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, RouteComponentProps } from 'react-router-dom';
import { getEntities } from '../../entities/aur-menu-role-rel/aur-menu-role-rel.reducer';
import { getRoles } from '../administration/user-management/user-management.reducer';
import { useAppSelector, useAppDispatch } from '../../config/store';

export const UserHome = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);

  const aurMenuRoleMenuList = useAppSelector(state => state.aurMenuRoleRel.entities);
  const aurMenuRoleRelList = useAppSelector(state => state.aurMenuRoleRel.entities);
  const loading = useAppSelector(state => state.aurMenuRoleRel.loading);

  useEffect(() => {
    dispatch(getRoles());
  }, []);

  useEffect(() => {
    dispatch(getRoles());
  }, []);

  function handleSyncList() {
    dispatch(getEntities({}));
  }

  return (
    <div>
      <h2>{account.login.toLowerCase()}</h2>

      <Alert color="danger"></Alert>

      <Alert color="success">
        <Button tag={Link} to="/aur-menu-role-rel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>

        <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
          <FontAwesomeIcon icon="sync" spin={loading} />{' '}
          <Translate contentKey="wmsApp.aurMenuRoleRel.home.refreshListLabel">Refresh List</Translate>
        </Button>

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
                    <td></td>
                    <td>{aurMenuRoleRel.menuId}</td>
                    <td>{aurMenuRoleRel.roleId}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container"></div>
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
      </Alert>
    </div>
  );
};
