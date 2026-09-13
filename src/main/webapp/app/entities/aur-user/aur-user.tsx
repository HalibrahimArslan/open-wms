import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities, updateEntity } from './aur-user.reducer';
import { IAurUser } from 'app/shared/model/aur-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurUser = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const aurUserList = useAppSelector(state => state.aurUser.entities);
  const loading = useAppSelector(state => state.aurUser.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="aur-user-heading" data-cy="AurUserHeading">
        <Translate contentKey="wmsApp.aurUser.home.title">Aur Users</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="wmsApp.aurUser.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="wmsApp.aurUser.home.createLabel">Create new Aur User</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {aurUserList && aurUserList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="wmsApp.aurUser.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurUser.userId">User Id</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurUser.userName">User Name</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.aurUser.companyCode">Company Code</Translate>
                </th>

                <th />
              </tr>
            </thead>
            <tbody>
              {aurUserList.map((aurUser, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${aurUser.id}`} color="link" size="sm">
                      {aurUser.id}
                    </Button>
                  </td>
                  <td>{aurUser.userId}</td>
                  <td>{aurUser.userName}</td>
                  <td>{aurUser.companyCode}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${aurUser.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${aurUser.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${aurUser.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="wmsApp.aurUser.home.notFound">No Aur Users found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AurUser;
