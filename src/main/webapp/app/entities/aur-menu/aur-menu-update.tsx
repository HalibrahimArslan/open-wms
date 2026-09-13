import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './aur-menu.reducer';
import { IAurMenu } from 'app/shared/model/aur-menu.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurMenuUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const aurMenuEntity = useAppSelector(state => state.aurMenu.entity);
  const loading = useAppSelector(state => state.aurMenu.loading);
  const updating = useAppSelector(state => state.aurMenu.updating);
  const updateSuccess = useAppSelector(state => state.aurMenu.updateSuccess);

  const handleClose = () => {
    props.history.push('/aur-menu');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...aurMenuEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...aurMenuEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="wmsApp.aurMenu.home.createOrEditLabel" data-cy="AurMenuCreateUpdateHeading">
            <Translate contentKey="wmsApp.aurMenu.home.createOrEditLabel">Create or edit a AurMenu</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="aur-menu-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('wmsApp.aurMenu.menuId')}
                id="aur-menu-menuId"
                name="menuId"
                data-cy="menuId"
                type="text"
              />
              <ValidatedField
                label={translate('wmsApp.aurMenu.parentMenuId')}
                id="aur-menu-parentMenuId"
                name="parentMenuId"
                data-cy="parentMenuId"
                type="text"
              />
              <ValidatedField
                label={translate('wmsApp.aurMenu.menuName')}
                id="aur-menu-menuName"
                name="menuName"
                data-cy="menuName"
                type="text"
              />
              <ValidatedField
                label={translate('wmsApp.aurMenu.menuType')}
                id="aur-menu-menuType"
                name="menuType"
                data-cy="menuType"
                type="text"
              />
              <ValidatedField
                label={translate('wmsApp.aurMenu.companyCode')}
                id="aur-menu-companyCode"
                name="companyCode"
                data-cy="companyCode"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/aur-menu" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AurMenuUpdate;
