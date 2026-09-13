import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './aur-menu.reducer';
import { IAurMenu } from 'app/shared/model/aur-menu.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurMenu = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const aurMenuList = useAppSelector(state => state.aurMenu.entities);
  const loading = useAppSelector(state => state.aurMenu.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="aur-menu-heading" data-cy="AurMenuHeading">
        <Translate contentKey="wmsApp.aurMenu.home.title">Aur Menus</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="wmsApp.aurMenu.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="wmsApp.aurMenu.home.createLabel">Create new Aur Menu</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {aurMenuList && aurMenuList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="wmsApp.aurMenu.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurMenu.menuId">Menu Id</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurMenu.parentMenuId">Parent Menu Id</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurMenu.menuName">Menu Name</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurMenu.menuType">Menu Type</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurMenu.companyCode">Company Code</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {aurMenuList.map((aurMenu, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${aurMenu.id}`} color="link" size="sm">
                      {aurMenu.id}
                    </Button>
                  </td>
                  <td>{aurMenu.menuId}</td>
                  <td>{aurMenu.parentMenuId}</td>
                  <td>{aurMenu.menuName}</td>
                  <td>{aurMenu.menuType}</td>
                  <td>{aurMenu.companyCode}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${aurMenu.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${aurMenu.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${aurMenu.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="wmsApp.aurMenu.home.notFound">No Aur Menus found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AurMenu;
