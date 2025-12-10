import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { OfferPromotionType } from 'app/shared/model/enumerations/offer-promotion-type.model';
import { createEntity, getEntity, reset, updateEntity } from './subscription-plan-offer.reducer';

export const SubscriptionPlanOfferUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userLites = useAppSelector(state => state.userLite.entities);
  const subscriptionPlanOfferEntity = useAppSelector(state => state.subscriptionPlanOffer.entity);
  const loading = useAppSelector(state => state.subscriptionPlanOffer.loading);
  const updating = useAppSelector(state => state.subscriptionPlanOffer.updating);
  const updateSuccess = useAppSelector(state => state.subscriptionPlanOffer.updateSuccess);
  const offerPromotionTypeValues = Object.keys(OfferPromotionType);

  const handleClose = () => {
    navigate(`/subscription-plan-offer${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    if (values.discountPercentage !== undefined && typeof values.discountPercentage !== 'number') {
      values.discountPercentage = Number(values.discountPercentage);
    }
    if (values.subscriptionsLimit !== undefined && typeof values.subscriptionsLimit !== 'number') {
      values.subscriptionsLimit = Number(values.subscriptionsLimit);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...subscriptionPlanOfferEntity,
      ...values,
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
        }
      : {
          promotionType: 'DISCOUNT',
          ...subscriptionPlanOfferEntity,
          createdDate: convertDateTimeFromServer(subscriptionPlanOfferEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(subscriptionPlanOfferEntity.lastModifiedDate),
          creator: subscriptionPlanOfferEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.subscriptionPlanOffer.home.createOrEditLabel" data-cy="SubscriptionPlanOfferCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.home.createOrEditLabel">
              Create or edit a SubscriptionPlanOffer
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
                  id="subscription-plan-offer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.freeDaysDuration')}
                id="subscription-plan-offer-freeDaysDuration"
                name="freeDaysDuration"
                data-cy="freeDaysDuration"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.discountPercentage')}
                id="subscription-plan-offer-discountPercentage"
                name="discountPercentage"
                data-cy="discountPercentage"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 100, message: translate('entity.validation.max', { max: 100 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.startDate')}
                id="subscription-plan-offer-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.endDate')}
                id="subscription-plan-offer-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.subscriptionsLimit')}
                id="subscription-plan-offer-subscriptionsLimit"
                name="subscriptionsLimit"
                data-cy="subscriptionsLimit"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.promotionType')}
                id="subscription-plan-offer-promotionType"
                name="promotionType"
                data-cy="promotionType"
                type="select"
              >
                {offerPromotionTypeValues.map(offerPromotionType => (
                  <option value={offerPromotionType} key={offerPromotionType}>
                    {translate(`monsterdamChicksApp.OfferPromotionType.${offerPromotionType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.createdDate')}
                id="subscription-plan-offer-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.lastModifiedDate')}
                id="subscription-plan-offer-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.createdBy')}
                id="subscription-plan-offer-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.lastModifiedBy')}
                id="subscription-plan-offer-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.isDeleted')}
                id="subscription-plan-offer-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                id="subscription-plan-offer-creator"
                name="creator"
                data-cy="creator"
                label={translate('monsterdamChicksApp.subscriptionPlanOffer.creator')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/subscription-plan-offer" replace color="info">
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

export default SubscriptionPlanOfferUpdate;
