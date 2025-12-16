import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getContentPackages } from 'app/entities/content-package/content-package.reducer';
import { getEntities as getPayments } from 'app/entities/payment/payment.reducer';
import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { createEntity, getEntity, reset, updateEntity } from './purchased-content.reducer';

export const PurchasedContentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const contentPackages = useAppSelector(state => state.contentPackage.entities);
  const payments = useAppSelector(state => state.payment.entities);
  const userLites = useAppSelector(state => state.userLite.entities);
  const purchasedContentEntity = useAppSelector(state => state.purchasedContent.entity);
  const loading = useAppSelector(state => state.purchasedContent.loading);
  const updating = useAppSelector(state => state.purchasedContent.updating);
  const updateSuccess = useAppSelector(state => state.purchasedContent.updateSuccess);

  const handleClose = () => {
    navigate(`/purchased-content${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getContentPackages({}));
    dispatch(getPayments({}));
    dispatch(getUserLites({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.rating !== undefined && typeof values.rating !== 'number') {
      values.rating = Number(values.rating);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...purchasedContentEntity,
      ...values,
      contentPackage: contentPackages.find(it => it.id.toString() === values.contentPackage?.toString()),
      payment: payments.find(it => it.id.toString() === values.payment?.toString()),
      viewer: userLites.find(it => it.id.toString() === values.viewer?.toString()),
      creator: userLites.find(it => it.id.toString() === values.creator?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          ...purchasedContentEntity,
          createdDate: convertDateTimeFromServer(purchasedContentEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(purchasedContentEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(purchasedContentEntity.deletedDate),
          contentPackage: purchasedContentEntity?.contentPackage?.id,
          payment: purchasedContentEntity?.payment?.id,
          viewer: purchasedContentEntity?.viewer?.id,
          creator: purchasedContentEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.purchasedContent.home.createOrEditLabel" data-cy="PurchasedContentCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.purchasedContent.home.createOrEditLabel">
              Create or edit a PurchasedContent
            </Translate>
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
                  id="purchased-content-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedContent.rating')}
                id="purchased-content-rating"
                name="rating"
                data-cy="rating"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedContent.createdDate')}
                id="purchased-content-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedContent.lastModifiedDate')}
                id="purchased-content-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedContent.createdBy')}
                id="purchased-content-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedContent.lastModifiedBy')}
                id="purchased-content-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedContent.deletedDate')}
                id="purchased-content-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="purchased-content-contentPackage"
                name="contentPackage"
                data-cy="contentPackage"
                label={translate('monsterdamChicksApp.purchasedContent.contentPackage')}
                type="select"
                required
              >
                <option value="" key="0" />
                {contentPackages
                  ? contentPackages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="purchased-content-payment"
                name="payment"
                data-cy="payment"
                label={translate('monsterdamChicksApp.purchasedContent.payment')}
                type="select"
              >
                <option value="" key="0" />
                {payments
                  ? payments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="purchased-content-viewer"
                name="viewer"
                data-cy="viewer"
                label={translate('monsterdamChicksApp.purchasedContent.viewer')}
                type="select"
              >
                <option value="" key="0" />
                {userLites
                  ? userLites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="purchased-content-creator"
                name="creator"
                data-cy="creator"
                label={translate('monsterdamChicksApp.purchasedContent.creator')}
                type="select"
              >
                <option value="" key="0" />
                {userLites
                  ? userLites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/purchased-content" replace color="info">
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

export default PurchasedContentUpdate;
