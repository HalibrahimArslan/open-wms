import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './warehouse.reducer';
import { IDepo } from 'app/shared/model/depo.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Depo = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const depoList = useAppSelector(state => state.depo.entities);
  const loading = useAppSelector(state => state.depo.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="warehouse-heading" data-cy="DepoHeading">
        <Translate contentKey="wmsApp.warehouse.home.title">Depos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="wmsApp.warehouse.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="wmsApp.warehouse.home.createLabel">Create new Depo</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {depoList && depoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="wmsApp.warehouse.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.warehouse.depoNo">Depo No</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.warehouse.depoName">Depo Name</Translate>
                </th>
                <th>
                  <Translate contentKey="wmsApp.warehouse.companyCode">Company Code</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {depoList.map((warehouse, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${warehouse.id}`} color="link" size="sm">
                      {warehouse.id}
                    </Button>
                  </td>
                  <td>{warehouse.code}</td>
                  <td>{warehouse.name}</td>
                  <td>{warehouse.companyCode}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${warehouse.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${warehouse.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${warehouse.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="wmsApp.warehouse.home.notFound">No Depos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Depo;
