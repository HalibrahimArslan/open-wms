import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './aur-company.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AurCompanyUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const aurCompanyEntity = useAppSelector(state => state.aurCompany.entity);
  const loading = useAppSelector(state => state.aurCompany.loading);
  const updating = useAppSelector(state => state.aurCompany.updating);
  const updateSuccess = useAppSelector(state => state.aurCompany.updateSuccess);

  const handleClose = () => {
    props.history.push('/aur-company');
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
      ...aurCompanyEntity,
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
          ...aurCompanyEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="wmsApp.aurCompany.home.createOrEditLabel" data-cy="AurCompanyCreateUpdateHeading">
            <Translate contentKey="wmsApp.aurCompany.home.createOrEditLabel">Create or edit a AurCompany</Translate>
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
                  id="aur-company-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('wmsApp.aurCompany.companyCode')}
                id="aur-company-companyCode"
                name="companyCode"
                data-cy="companyCode"
                type="text"
              />
              <ValidatedField
                label={translate('wmsApp.aurCompany.companyName')}
                id="aur-company-companyName"
                name="companyName"
                data-cy="companyName"
                type="text"
              />
              <ValidatedField
                label={translate('wmsApp.aurCompany.erpTipi')}
                id="aur-company-erpTipi"
                name="erpTipi"
                data-cy="erpTipi"
                type="text"
              />
              <ValidatedField
                label={translate('wmsApp.aurCompany.apiEndPoint')}
                id="aur-company-apiEndPoint"
                name="apiEndPoint"
                data-cy="apiEndPoint"
                type="text"
              />
              <ValidatedField
                label={translate('wmsApp.aurCompany.apiParameters')}
                id="aur-company-apiParameters"
                name="apiParameters"
                data-cy="apiParameters"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/aur-company" replace color="info">
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

export default AurCompanyUpdate;
