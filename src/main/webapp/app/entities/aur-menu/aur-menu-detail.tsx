import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './aur-menu.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurMenuDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const aurMenuEntity = useAppSelector(state => state.aurMenu.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aurMenuDetailsHeading">
          <Translate contentKey="wmsApp.aurMenu.detail.title">AurMenu</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aurMenuEntity.id}</dd>
          <dt>
            <span id="menuId">
              <Translate contentKey="wmsApp.aurMenu.menuId">Menu Id</Translate>
            </span>
          </dt>
          <dd>{aurMenuEntity.menuId}</dd>
          <dt>
            <span id="parentMenuId">
              <Translate contentKey="wmsApp.aurMenu.parentMenuId">Parent Menu Id</Translate>
            </span>
          </dt>
          <dd>{aurMenuEntity.parentMenuId}</dd>
          <dt>
            <span id="menuName">
              <Translate contentKey="wmsApp.aurMenu.menuName">Menu Name</Translate>
            </span>
          </dt>
          <dd>{aurMenuEntity.menuName}</dd>
          <dt>
            <span id="menuType">
              <Translate contentKey="wmsApp.aurMenu.menuType">Menu Type</Translate>
            </span>
          </dt>
          <dd>{aurMenuEntity.menuType}</dd>
          <dt>
            <span id="companyCode">
              <Translate contentKey="wmsApp.aurMenu.companyCode">Company Code</Translate>
            </span>
          </dt>
          <dd>{aurMenuEntity.companyCode}</dd>
        </dl>
        <Button tag={Link} to="/aur-menu" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aur-menu/${aurMenuEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AurMenuDetail;
